import './App.css';
import Login from './Components/Login';
import Dashboard from './Components/Dashboard';
import { useState, useEffect } from 'react';
import { auth } from './fire';

function App() {
	const [user, setUser] = useState();

	useEffect(() => {
		auth.onAuthStateChanged(function (user) {
			if (user) {
				setUser(user);
			} else {
				console.log('You are not logged in.');
			}
		});
	}, [user]);

	return (
		<>{user ? <Dashboard setUser={setUser} /> : <Login setUser={setUser} />}</>
	);
}

export default App;

// https://stackoverflow.com/questions/37883981/cant-get-currentuser-on-load
