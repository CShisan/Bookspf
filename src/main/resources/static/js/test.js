var order = new Vue({
    el: '#orders',
    data: {
        orderList: []
    },
    methods: {
        getOrders() {
            var that = this;
            axios.post("/orders")
                .then(response => {
                    console.log(response);
                })
        }
    }
});