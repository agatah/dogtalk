function enableButton(){
    let inputPhoto;
    inputPhoto = document.getElementById('photo');
    if(inputPhoto.value != null){
        document.getElementById('sendPhoto').disabled = false;
    } else {
        document.getElementById('sendPhoto').disabled = true;
    }
}