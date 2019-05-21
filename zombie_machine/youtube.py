import requests
import webbrowser


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
    new = 2  # open in a new tab, if possible
    webbrowser.get(using='chrome').open(url)


vid_url = get_latest_video("EmisijeRTVojvodine")
print(vid_url)
open_in_browser(vid_url)
