
document.addEventListener('DOMContentLoaded', function() {

    const inputs = document.querySelectorAll('input, textarea');
    inputs.forEach(input => {
        input.addEventListener('focus', () => {
            input.style.borderColor = '#007bff';
        });
        input.addEventListener('blur', () => {
            input.style.borderColor = '#ccc';
        });
    });
});

 function initializeImageUpload() {
     $(document).ready(function() {
         $('#productImage').click(function() {
             $('#file').click();
         });

         $('#file').change(function(event) {
             if (event.target.files && event.target.files[0]) {
                 var reader = new FileReader();
                 reader.onload = function(e) {
                     $('#productImage').attr('src', e.target.result);
                 }
                 reader.readAsDataURL(event.target.files[0]);
             }
         });
     });
 }
