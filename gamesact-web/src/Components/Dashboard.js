import React from 'react';
import flame from '../Images/flame.svg';
import { firestore, auth } from '../fire';
import { Button } from '@material-ui/core';
import { useState, useEffect } from 'react';
import Order from './Order';

const Dashboard = ({ setUser }) => {
	const [orders, setOrders] = useState([]);

	//useEffet to fetch data
	useEffect(() => {
		const orders2 = [];
		// https://firebase.google.com/docs/firestore/query-data/get-data
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
			})
			.catch((error) => {
				console.log('Error getting documents: ', error);
			});
	}, []);

	//handlers
	const logout = () => {
		auth
			.signOut()
			.then(() => {
				setUser();
			})
			.catch((error) => {
				console.log('Logout error: ' + error.message);
			});
	};

	return (
		<>
			<header>
				<nav>
					<img src={flame} alt='Logo' /> <h2> GamesAct</h2>
				</nav>

				<h3>Pending Orders</h3>
				<Button variant='contained' color='secondary' onClick={logout}>
					Logout
				</Button>
			</header>

			<div className='orders-list'>
				{orders.map((order) => (
					<Order order={order} key={order.id} setOrders={setOrders} />
				))}
			</div>
		</>
	);
};

export default Dashboard;

// ref
// https://stackoverflow.com/questions/57508459/cant-setstate-firestore-data
