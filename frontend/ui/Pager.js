export function renderPager(total, page, pageSize, onGo) {
    const pages = Math.max(1, Math.ceil(total / pageSize));
    const wrap = document.createElement('div');

    wrap.style.display = 'flex';
    wrap.style.gap = '.5rem';
    wrap.style.alignItems = 'center';
    wrap.style.marginTop = '1rem';
    
    const btn = (label, disabled, goTo) => {
      const btnEl = document.createElement('button');
      btnEl.textContent = label;
      btnEl.disabled = disabled;
      btnEl.onclick = () => onGo(goTo);
      return btnEl;
    };

    wrap.append(
      btn('« First', page <= 1, 1),
      btn('‹ Prev', page <= 1, page - 1),
      span(`${page} / ${pages}`),
      btn('Next ›', page >= pages, page + 1),
      btn('Last »', page >= pages, pages),
    );
    return wrap;
}
  
function span(text) { 
    const spanEl = document.createElement('span'); 
    spanEl.textContent = text; 
    return spanEl; 
}