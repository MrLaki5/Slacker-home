from uuid import getnode

def get_mac():
    mac = getnode ()
    real_mac = ':'.join(("%012X" % mac)[i:i+2] for i in range(0, 12, 2))
    return real_mac
