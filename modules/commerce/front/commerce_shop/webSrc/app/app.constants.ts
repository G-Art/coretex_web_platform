import {environment} from '../environments/environment';

export class App {

    static API = class {
        public static login = `${environment.baseApiUrl}/login`;
        public static logout = `${environment.baseApiUrl}/logout`;
        public static signUp = `${environment.baseApiUrl}/register`;

        public static currentUser = `${environment.baseApiUrl}/user/current`;
        public static userLanguage = `${environment.baseApiUrl}/user/language`;
        public static currentStore = `${environment.baseApiUrl}/stores/current`;
        public static currentCart = `${environment.baseApiUrl}/cart/current`;

        public static cartUpdate = `${environment.baseApiUrl}/cart/update`;
        public static cartAdd = `${environment.baseApiUrl}/cart/add`;
        public static cartDeliveryType = `${environment.baseApiUrl}/cart/delivery/type`;
        public static cartPlaceOrder = `${environment.baseApiUrl}/cart/placeOrder`;
        public static cartDeliveryInfo = `${environment.baseApiUrl}/cart/delivery/info`;
        public static cartPaymentType = `${environment.baseApiUrl}/cart/payment/type`;

        public static menu = `${environment.baseApiUrl}/categories/menu`;

        public static deliveryServiceCart = uuid => `${environment.baseApiUrl}/delivery/service/cart/${uuid}`;

        public static deliveryServiceTypes = uuid => `${environment.baseApiUrl}/delivery/service/${uuid}/types`;
        public static deliveryTypePayments = code => `${environment.baseApiUrl}/delivery/type/${code}/payments`;

        public static productByCode = code => `${environment.baseApiUrl}/product/${code}`;

        public static searchCategory = (code, page) => `${environment.baseApiUrl}/categories/${code}/page${page ? '/' + page : ''}`;

        public static searchText = page => `${environment.baseApiUrl}/search/page${page ? '/' + page : ''}`;

        public static storeLanguages = uuid => `${environment.baseApiUrl}/languages/store/${uuid}`;
    }
}
