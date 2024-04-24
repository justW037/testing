function addFocus(selector) {
  const inputs = document.querySelectorAll(selector)

  inputs.forEach(input => {
    input.addEventListener('focus', function () {
      this.parentNode.style.border = '1px solid #00ff7f'
    })

    input.addEventListener('blur', function () {
      this.parentNode.style.border = ''
    })
  })
}

addFocus('.form-inp input')
