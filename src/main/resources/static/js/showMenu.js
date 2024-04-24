function showMenu(selector) {
  const buttons = document.querySelectorAll(selector)

  buttons.forEach(button => {
    button.addEventListener('click', function () {
      const menu = this.nextElementSibling
      const allMenus = document.querySelectorAll('.menu')
      allMenus.forEach(otherMenu => {
        if (otherMenu !== menu && otherMenu.style.display === 'block') {
          otherMenu.style.display = 'none'
        }
      })
      if (menu.style.display === 'none') {
        menu.style.display = 'block'
      } else {
        menu.style.display = 'none'
      }
    })
  })
}
showMenu('.test')
