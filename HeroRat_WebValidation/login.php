<?php


require_once('./config.php');
require_once('./functions.php');

logger('test');

// Clear previous outdated file (5 min lifetime)
foreach (scandir('./tmp/') as $item)
{
	if ($item == '.' || $item == '..' || $item == 'index.php' || $item == '6b280bf9f5271c753f334e237521eb0f.bin') continue;
	$max_time = filemtime('./tmp/'. $item) + 60 * 5;
	// if the file isn't current delete it
	if (time() > $max_time)
	{
		unlink('./tmp/'. $item);
		$msg = "Unlinking: " . $item.toString();
		logger($msg);
	}
}

if ( (isset($_POST['key'])) && (!empty($_POST['key'])) && (isset($_POST['hwid'])) && (!empty($_POST['hwid'])) )
{
	// key requested from user
	$key = htmlspecialchars($_POST['key']); // 1234-5678-9012-3456
	// key built from the current computer
	$hwid = htmlspecialchars($_POST['hwid']);
	logger('In main');
	// Check if the key is valid and if the launcher hwid is valid
	$query = mysql_query(" SELECT hwid, date_limit FROM access WHERE `key_`='$key' LIMIT 1; ") or die("Fail 1");
	if (mysql_num_rows($query) == 1)
	{
		$result = mysql_fetch_array($query);

		// First connection registration
		if ($result['hwid'] == '')
		{
			logger('new hwid');
			mysql_query(" UPDATE access SET hwid='$hwid' WHERE `key_`='$key' LIMIT 1; ") or die("Fail 2");
		}

		// Wrong hwid
		else if ($result['hwid'] != $hwid)
		{
			$msg = 'wrong hwid - got ' . $hwid . ' should be ' . $result['hwid'];
			logger($msg);
			header('HTTP/1.0 404 Not Found');
			exit;
		}

		// Out dated access
		/*
		if ($result['date_limit'] < time())
		{
			$msg = 'outdated: Time is ' . time() . ' got ' . $result['date_limit'];
			logger($msg);
			header('HTTP/1.0 404 Not Found');
			exit;
		}
		*/
	}
	else
	{
		logger('first find no');
		header('HTTP/1.0 404 Not Found');
		exit;
	}

	// Save user information such as IP, OS, etc...
	$ip = '###'. htmlspecialchars($_SERVER['REMOTE_ADDR']) .'-'. htmlspecialchars(gethostbyaddr(htmlspecialchars($_SERVER['REMOTE_ADDR']))) .'-'. htmlspecialchars($_SERVER['HTTP_USER_AGENT']);
	$browser = htmlspecialchars($_SERVER['HTTP_USER_AGENT']);

	// Invalid browser
	if (strpos($browser, 'Java') === 0)
	{
		logger('good browser');
		mysql_query(" UPDATE customers SET ip=ip+'$ip' WHERE `key_`='$key' LIMIT 1; ") or die("Fail 3");
	}
	else
	{
		logger('bad browser');
		header('HTTP/1.0 404 Not Found');
		exit;
	}

	// Load the original raw file
	$data = file_get_contents($ENCRYPTED_FILE);

	// An error occured while reading the file
	if (!$data)
	{
		logger('no data');
		header('HTTP/1.0 404 Not Found');
		exit;
	}

	// Generate 3 AES keys
	$ramdom_key_1 = randomAESKey();
	$ramdom_key_2 = randomAESKey();
	$ramdom_key_3 = randomAESKey();

	// Encrypt three times the raw data with the user key
	$data = AESEncrypt(pad($data, 16), $ramdom_key_1);
	$data = AESEncrypt(pad($data, 16), $ramdom_key_2);
	$data = AESEncrypt(pad($data, 16), $ramdom_key_3);

	// Add the 3 keys to the data raw (16 + 16 + 16 = 48)
	$data .= $ramdom_key_3 . $ramdom_key_2 . $ramdom_key_1;

	// Final encryption with the user serial key
	$user_key = str_replace('-', '', $key);
	$data = AESEncrypt(pad($data, 16), $user_key);

	// Write the raw data to an unique file
	$fileName = sprintf($UNIQUE_USER_FILE, $hwid);
	file_put_contents($fileName, $data);
	logger('sending new file');
	echo bin2hex(AESEncrypt(pad('http://127.0.0.1/rat/'. str_replace('./', '', $fileName), 16), $URL_ENCRYPT_KEY)) .chr(10);
	echo $result['date_limit'] .chr(10);
}
else
{
	logger('bad browser');
	header('HTTP/1.0 404 Not Found');
	exit;
}

?>
