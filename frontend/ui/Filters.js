export function renderFilters(meta, onSearch) {
    const form = document.createElement('form');
    form.style.display = 'grid';
    form.style.gap = '.5rem';
    form.style.gridTemplateColumns = 'repeat(auto-fit, minmax(160px, 1fr))';
  
    const makeSel = select('Any Make', meta.makes);
    const bodySel = select('Any Body', meta.bodies);
    const seatsIn = input('number', 'Min seats');
    const priceIn = input('number', 'Max price');
    const sortSel = select('Sort by', ['msrp','year']);
    const orderSel = select('Order', ['asc','desc']);
  
    form.append(makeSel, bodySel, seatsIn, priceIn, sortSel, orderSel);
  
    const btn = document.createElement('button');
    btn.type = 'submit';
    btn.textContent = 'Search';
    form.append(btn);
  
    form.addEventListener('submit', (e) => {
      e.preventDefault();
      onSearch({
        make: valueOrUndefined(makeSel.value),
        body: valueOrUndefined(bodySel.value),
        minSeats: valueOrUndefined(seatsIn.value),
        maxPrice: valueOrUndefined(priceIn.value),
        sort: valueOrUndefined(sortSel.value) || 'msrp',
        order: valueOrUndefined(orderSel.value) || 'asc',
      });
    });
  
    return form;
  }
  
  function select(placeholder, options) {
    const el = document.createElement('select');
    const opt0 = document.createElement('option');
    opt0.value = '';
    opt0.textContent = placeholder;
    el.append(opt0);
    options.forEach(o => {
      const opt = document.createElement('option');
      opt.value = o;
      opt.textContent = o;
      el.append(opt);
    });
    return el;
  }
  
  function input(type, placeholder) {
    const el = document.createElement('input');
    el.type = type; el.placeholder = placeholder;
    return el;
  }
  
  function valueOrUndefined(value) {
    return value === '' ? undefined : value;
  }