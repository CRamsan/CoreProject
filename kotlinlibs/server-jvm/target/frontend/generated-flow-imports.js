const div = document.createElement('div');
div.innerHTML = '<custom-style>    <style include="lumo-color lumo-typography"></style></custom-style>';
document.head.insertBefore(div.firstElementChild, document.head.firstChild);

import '@vaadin/vaadin-ordered-layout/theme/lumo/vaadin-vertical-layout.js';