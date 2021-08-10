function previewPhotoAndEnableSendButton(fileInput, fileOutput, sendButton){
    let inputPhoto = document.getElementById(fileInput);
    let output = document.getElementById(fileOutput);
    if(inputPhoto.value != null){
        document.getElementById(sendButton).disabled = false;
        output.src = URL.createObjectURL(event.target.files[0]);
        output.onload = function() {
            URL.revokeObjectURL(output.src) // free memory
        }
    } else {
        document.getElementById(sendButton).disabled = true;
    }
}