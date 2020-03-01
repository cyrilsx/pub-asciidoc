import Vue from 'vue';
import vuetify from './plugins/vuetify';

Vue.use(vuetify);

import App from './App.vue';
import router from './router';

import 'vuetify/dist/vuetify.min.css';

Vue.config.productionTip = false;


new Vue({
    router,
    vuetify,
    render: (h) => h(App),
}).$mount('#app');
