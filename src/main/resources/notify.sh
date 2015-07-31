msg=`cat $1/message.properties | awk -f $1/find_message.sh -v key="$2"`
subt=`echo $3`
echo $msg
osascript -e "display notification \"$msg\" with title \"Notification\" subtitle \"$subt\""
say -v Ting-Ting $msg
