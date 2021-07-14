import firebase from 'firebase/app';
import 'firebase/auth';
import 'firebase/firestore';

const app = firebase.initializeApp({
	apiKey: 'AIzaSyBCtcqlCu3ndibBbCv-ZChOz9MT4uNjrtg',
	authDomain: 'game-sact.firebaseapp.com',
	projectId: 'game-sact',
	storageBucket: 'game-sact.appspot.com',
	messagingSenderId: '1068154736144',
	appId: '1:1068154736144:web:673901b95c98449ade8495',
	measurementId: 'G-BEY802VDYJ',
});

export const auth = app.auth();
export const firestore = app.firestore();

export default app;
