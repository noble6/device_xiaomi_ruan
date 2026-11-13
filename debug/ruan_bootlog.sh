#!/vendor/bin/sh
# Debug builds: keep the live logs on /metadata, which recovery can read
# (it can't unlock /data). Every 5 s for 10 minutes after post-fs-data.
D=/metadata/bootlog
mkdir -p $D
for i in $(seq 1 120); do
    /system/bin/logcat -d -b all > $D/logcat.txt 2>&1
    dmesg > $D/dmesg.txt 2>&1
    getprop > $D/props.txt 2>&1
    ps -A -o PID,USER,LABEL,NAME > $D/ps.txt 2>&1
    p=$(pidof adbd)
    if [ -n "$p" ]; then
        { cat /proc/$p/status; cat /proc/$p/limits; ls -l /proc/$p/fd | wc -l; } > $D/adbd.txt 2>&1
    fi
    sync
    sleep 5
done
