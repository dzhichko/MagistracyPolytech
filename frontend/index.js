
function searchClick() {
    const searchValue = document.getElementById('searchInput').value.trim().toUpperCase();
    const contentPanels = document.querySelectorAll('.course-panel');
    let count = 0; // Правильное расположение счетчика

    contentPanels.forEach(panel => {
        const linkElement = panel.querySelector('.main-information a');
        if (!linkElement) return;

        if (!linkElement.dataset.originalHtml) {
            linkElement.dataset.originalHtml = linkElement.innerHTML;
        }

        const courseNumber = linkElement.querySelector('.number-of-course')?.textContent.trim().toUpperCase() || '';
        const courseName = linkElement.textContent.replace(courseNumber, '').trim().toUpperCase();

        const matchesName = courseName.includes(searchValue);
        const matchesNumber = courseNumber.includes(searchValue);

        if (matchesNumber || matchesName) {
            panel.style.display = 'block';
            count++;
        } else {
            panel.style.display = 'none';
        }
    });

    updateCount(count); // Обновляем счетчик один раз после цикла
}



document.getElementById('searchInput').addEventListener('keyup', function(e) {
    if (e.key === 'Enter') searchClick();
});

function updateCount(count){
    document.querySelector('.search-bar .resultOfSearch .count').textContent = count;
}

function clearInput() {

    const searchValue = document.getElementById('searchInput')
    searchValue.value='';

    document.querySelector('.search-bar .resultOfSearch .count').textContent = '14';

    document.querySelectorAll('.course-panel').forEach(panel => {
        panel.style.display= 'block';
        const linkElement = panel.querySelector('.main-information a');
        if (linkElement && linkElement.dataset.originalHtml) {
            linkElement.innerHTML = linkElement.dataset.originalHtml;
        }

    });

}