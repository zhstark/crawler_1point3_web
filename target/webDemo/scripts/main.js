var rootUrl = window.location.href.split('?')[0]
let counter = 0;
let i = 0;
for (; i < rootUrl.length; i++) {
    if (counter == 4) {
        break;
    }
    if (rootUrl.charAt(i) == '/') {
        counter += 1;
    }
}
rootUrl = rootUrl.substring(0,i);