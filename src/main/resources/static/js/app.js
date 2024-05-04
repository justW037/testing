function closePopup() {
  const popup = document.getElementById('popup')
  const form = popup.querySelector('form')

  if (popup.style.display === 'none') {
    popup.style.display = 'block'
  } else {
    popup.style.display = 'none'
    form.reset()
  }
}
function closePopup2() {
  const popup = document.getElementById('popup2')
  const form = popup.querySelector('form')

  if (popup.style.display === 'none') {
    popup.style.display = 'block'
  } else {
    popup.style.display = 'none'
    form.reset()
  }
}
document.addEventListener('DOMContentLoaded', function () {
  const editButtons = document.querySelectorAll('.menu li:first-child')

  editButtons.forEach(button => {
    button.addEventListener('click', function () {
      const studentRow = this.closest('tr')
      const studentId = studentRow.querySelector('td:first-child').innerText
      const studentName = studentRow.querySelector('td:nth-child(2)').innerText
      const studentEmail = studentRow.querySelector('td:nth-child(3)').innerText
      const studentPhone = studentRow.querySelector('td:nth-child(4)').innerText
      const studentBirthday =
        studentRow.querySelector('td:nth-child(5)').innerText
      const studentAddress =
        studentRow.querySelector('td:nth-child(6)').innerText
      const studentSex = studentRow.querySelector('td:nth-child(7)').innerText

      const form = document.getElementById('edit-form')
      form.elements.name.value = studentName
      form.elements.email.value = studentEmail
      form.elements.phone.value = studentPhone
      form.elements.birthday.value = studentBirthday
      form.elements.address.value = studentAddress
      form.elements.sex.value = studentSex === 'Nam' ? 0 : 1
      form.elements.id.value = studentId

      closePopup2()
    })
  })
})

const errorMessageElement = document.querySelector('.error-messages')
if (errorMessageElement) {
  setTimeout(function () {
    errorMessageElement.style.display = 'none'
  }, 5000)
}
