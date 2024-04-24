function validatePass() {
  let password = document.getElementById('password');
  let confirm_password = document.getElementById('retypePassword');

  function validatePassword() {
    if (!confirm_password) {
      if (password.value.length < 8 || password.value.length > 12) {
        password.setCustomValidity('Mật khẩu phải có từ 8 đến 12 ký tự');
      } else {
        password.setCustomValidity('');
      }
    } else {
      if (password.value != confirm_password.value) {
        confirm_password.setCustomValidity('Mật khẩu không trùng khớp');
      } else {
        confirm_password.setCustomValidity('');
      }
    }
  }
  if (!confirm_password) {
    password.onchange = validatePassword;
  } else {
    password.onchange = validatePassword;
    confirm_password.onkeyup = validatePassword;
  }
}

validatePass() 