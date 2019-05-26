import subprocess
from threading import Thread

def play_function(command):
	subprocess.call(command, creationflags=subprocess.CREATE_NEW_CONSOLE)

def play_in_mpv(link, is_full_screen, is_loop):
	command = "mpv " + link
	if is_full_screen:
		command = command + " --fs"
	if is_loop:
		command = command + " --loop"
	# os.system(command + " && exit")
	Thread(target = play_function, args = (command, )).start()
