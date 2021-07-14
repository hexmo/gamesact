import React from 'react';
import { firestore } from '../fire';
import { Button, Chip } from '@material-ui/core';
import FaceIcon from '@material-ui/icons/Face';
import AttachMoneyIcon from '@material-ui/icons/AttachMoney';
import axios from 'axios';

const Order = ({ order, setOrders }) => {
	// handlers
	const completeOrderHandler = (order) => {
		console.log(order.id);
		firestore
			.collection('orders')
			.doc(order.id)
			.update({ orderCompleted: true })
			.then(() => {
				// update orders list
				const orders2 = [];
				firestore
					.collection('orders')
					.where('orderCompleted', '==', false)
					.get()
					.then((querySnapshot) => {
						querySnapshot.forEach((doc) => {
							const order = { id: doc.id, data: doc.data() };
							orders2.push(order);
						});

						setOrders(orders2);
					});

				sendNotification(order);
			})
			.catch((error) => {
				// The document probably doesn't exist.
				console.error('Error updating document: ', error);
			});
	};

	// send notification to user about their order completion
	const sendNotification = (order) => {
		const message = {
			notification: {
				title: 'Purchase order sucessfully processed.',
				body: `${order.data.productName} sucessfully purchased.`,
				sound: 'default',
			},
			to: `/topics/${order.data.buyersId}`,
		};

		const headers = {
			'Content-Type': 'application/json',
			Authorization:
				'key=AAAA-LL7zhA:APA91bFuxIMniKfvLUd5EYD_UksZHA_KjrQKNJZ-96JAi4MICjxxBsh8aR9kD72-9Uin5CCLog5bDu3o_PebXO8LhVF1hZPVzM-5pRuOklb-HMjU-1v8KRsM37Tb4Y3LrllS-x1gJ9Zo',
		};

		axios
			.post('https://fcm.googleapis.com/fcm/send', message, {
				headers,
			})
			.then((response) => console.log(response))
			.catch((error) => console.error('Error caught by catch:', error));
	};

	return (
		<div className='order'>
			<div className='order-details'>
				<h3>{order.data.productName}</h3>
				<div className='gap' />
				<Chip
					icon={<AttachMoneyIcon />}
					label={`Rs ${order.data.nepaliPrice}`}
					color='primary'
				/>
				<div className='gap' />
				<Chip icon={<FaceIcon />} label={order.data.gamerTag} />
			</div>
			<Button
				variant='contained'
				color='primary'
				onClick={() => {
					completeOrderHandler(order);
				}}
			>
				Mark as completed
			</Button>
		</div>
	);
};

export default Order;

// Update a document
// https://firebase.google.com/docs/firestore/manage-data/add-data#update-data
// Send POST request to FCM for notification
// https://stackoverflow.com/questions/37371990/how-can-i-send-a-firebase-cloud-messaging-notification-without-use-the-firebase
// https://stackoverflow.com/questions/39824128/firebase-notifications-to-ios-topic-via-http-post-request
