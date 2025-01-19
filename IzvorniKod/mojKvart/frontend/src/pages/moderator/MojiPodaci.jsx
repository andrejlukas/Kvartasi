import { Navbar } from "../../components/ModeratorNavbar";
import { useState, useEffect } from "react";
import "../../styles/MojiPodaci.css";

export function ModeratorMojiPodaci() {
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [emailAddress, setEmailAddress] = useState('');
    const [errorMessage, setErrorMessage] = useState('');

    useEffect(() => {
        const token = localStorage.getItem('token');
        const options = {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ oneLiner: token })
        };

        fetch('/api/tokens/claims', options)
            .then(async response => {
                if (!response.ok) {
                    const text = await response.text();
                    throw new Error(text);
                }
                return response.json();
            })
            .then(data => {
                setEmailAddress(data.email);
            })
            .catch(error => setErrorMessage(error.message));
    }, []);

    useEffect(() => {
        const token = localStorage.getItem('token');
        const options = {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        };

        if (emailAddress) {
            fetch(`/api/moderators/${emailAddress}`, options)
                .then(response => {
                    if (!response.ok) {
                        const text = response.text();
                        throw new Error(text);
                    }
                    return response.json();
                })
               .then(data => {
                    setFirstName(data.moderatorIme);
                    setLastName(data.moderatorPrezime);
                })
                .catch(error => setErrorMessage(error.message));
        }
    }, [emailAddress]);

    return (
        <div className="moji-podaci-wrapper">
            <Navbar />
            <div className="main-container">
                <div className="osobni-podaci-container">
                    <h1 className="naslov">Osobni podaci:</h1>
                    <div className="moji-podaci-form-group">
                        <label>Ime:</label>
                        <input type="text" placeholder={firstName} className="moji-podaci-inputs" name="firstName" value={firstName} readOnly />
                    </div>
                    <div className="moji-podaci-form-group">
                        <label>Prezime:</label>
                        <input type="text" placeholder={lastName} className="moji-podaci-inputs" name="lastname" value={lastName} readOnly />
                    </div>
                    <div className="moji-podaci-form-group">
                        <label>Email:</label>
                        <input type="text" placeholder={emailAddress} className="moji-podaci-inputs" name="emailAddress" value={emailAddress} readOnly />
                    </div>
                    {errorMessage && <p style={{ color: 'red' }}>{errorMessage}</p>}
                </div>
            </div>
        </div>
    );
}