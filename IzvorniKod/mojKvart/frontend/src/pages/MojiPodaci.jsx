import { Navbar } from "../components/Navbar";
import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import "../styles/MojiPodaci.css"

export function MojiPodaci(){
    const navigate = useNavigate();
    const [firstName, setFirstName] = useState('')
    const [lastName, setLastName] = useState('')
    const [homeAddress, setHomeAddress] = useState('')
    const [emailAddress, setEmailAddress] = useState('')
    const [id, setId] = useState(null)
    const [sifra, setSifra] = useState('')
    const [errorMessage, setErrorMessage] = useState('');
    const [hasChanges, setHasChanges] = useState(false);
   
    //dohvacanje email-a
    useEffect(() => {
        const token = localStorage.getItem('token');
        var options = {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ token: token })
        }

        fetch('/api/tokens', options)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                setEmailAddress(data.email);
            })
            .catch(error => console.error('There was a problem with the fetch operation: ', error));
    }, []); //prazno, radi se na pocetku samo

    useEffect(() => {
        const token = localStorage.getItem('token');
        const options = {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        };
    
        // Dohvaćanje korisničkih podataka ako postoji email
        if (emailAddress) {
            fetch(`/api/kupacs/${emailAddress}`, options)
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Network response was not ok');
                    }
                    return response.json();
                })
                .then(data => {
                    setFirstName(data.kupacIme);
                    setLastName(data.kupacPrezime);
                    setHomeAddress(data.kupacAdresa == null ? "" : data.kupacAdresa);
                    setSifra(data.kupacSifra);
                    setId(data.kupacId);
                })
                .catch(error => {
                    console.error('There was a problem with the fetch operation: ', error);
                });
        }
    }, [emailAddress]);

   //spremanje promjena -> slanje promjena na backend
   function savePromjene(e){
        e.preventDefault();

        // Provjera praznih polja
        if (!firstName || !lastName || !homeAddress || !emailAddress) {
            setErrorMessage("Sva polja moraju biti popunjena.");
            return; // Zaustavlja submit ako neka polja nisu popunjena
        }

        // Ako su sva polja popunjena, resetirajte poruku o grešci i nastavite s API pozivom
        setErrorMessage('');

        const token = localStorage.getItem('token');
        const options ={
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify({
                kupacIme: firstName,
                kupacPrezime: lastName,
                kupacAdresa: homeAddress,
                kupacEmail: emailAddress,
                kupacId : id,
                kupacSifra:sifra

            })
        }
        fetch(`/api/kupacs/${id}`, options)
        .then(response => response.ok ? response.json() : Promise.reject('Failed to save changes'))
        .then(updated => {
            setHasChanges(false);
            alert("Promjene unesene!");
        })
        .catch(error => {
            alert("Google Oauth ne dopušta promjenu imena i prezimena!");
            console.error('Error updating data:', error);
        })

        navigate('/korisnickiracun');
   }

   function handleClose(){
        navigate('/korisnickiracun');
        return;
   }

   function handleInputChange(setter, value){
    setter(value);
    setHasChanges(true); 
   }

    return(
        <div>
            <Navbar/>
            <div className="main-container">
                    <div className="osobni-podaci-container">
                        <h1 className="naslov">Osobni podaci:</h1>
                        <form id="moji-podaci-profile-form" onSubmit = {savePromjene}>
                            <div className="moji-podaci-form-group">
                                <label >Ime:</label>
                                <input type="text"  placeholder={firstName} className="moji-podaci-inputs" name="firstName" value={firstName}
                                onChange={(e) => handleInputChange(setFirstName,e.target.value)} />
                            </div>
                            
                            <div className="moji-podaci-form-group">
                                <label >Prezime:</label>
                                <input type="text"  placeholder={lastName} className="moji-podaci-inputs" name="lastname" value={lastName}
                                onChange={(e) => handleInputChange(setLastName, e.target.value)} />
                            </div>
                            
                            <div className="moji-podaci-form-group">
                                <label >Email:</label>
                                <input type="text"  placeholder={emailAddress} className="moji-podaci-inputs" name="emailAddress" value={emailAddress}
                                 readOnly />
                            </div>
                            
                            <div className="moji-podaci-form-group">
                                <label >Adresa:</label>
                                <input type="text"  placeholder={homeAddress} className="moji-podaci-inputs" name="homeAddress" value={homeAddress}
                                onChange={(e) => handleInputChange(setHomeAddress,e.target.value)} />
                            </div>
                            {/* Prikaz poruke o grešci ako postoji */}
                            {errorMessage && <p style={{ color: 'red' }}>{errorMessage}</p>}
                            <div className="moji-podaci-gumbovi-container">
                                <button type="submit" id="spremi-promjene-button">Spremi promjene</button> 
                                <button 
                                    type="button" 
                                    id = "zatvori-button"
                                    onClick={handleClose} 
                                    disabled={!!errorMessage || hasChanges }>Zatvori</button>
                            </div>
                            
                           
                        </form>

                    </div>
                </div>
        </div>
    )
    
}