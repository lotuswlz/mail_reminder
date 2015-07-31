#!awk -f
BEGIN {
# param: key
}
{
    if (index($0,key "=")>0) {
        split($0,value_array,"=");
        print value_array[2];
    }
}
END {


}
