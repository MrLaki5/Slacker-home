import requests
import webbrowser
import platform


def get_videos(username):
    url = "https://www.youtube.com/user/" + username + "/videos"
    page = requests.get(url).content
    data = str(page).split(' ')
    item = 'href="/watch?'
    vids = [line.replace('href="', 'youtube.com') for line in data if item in line] # list of all videos listed twice
    return vids


def get_latest_video(username):
    vids = get_videos(username)
    return "https://www." + vids[0][:-1]


def open_in_browser(url):
    if platform.system() == "Windows":
        chrome_path = "C:/Program Files (x86)/Google/Chrome/Application/chrome.exe %s"
    else:
        if platform.system() == "Darwin":
            chrome_path = "open -a /Applications/Google\ Chrome.app %s"
        else:
            chrome_path = "/usr/bin/google-chrome %s"
    webbrowser.get(chrome_path).open(url)
