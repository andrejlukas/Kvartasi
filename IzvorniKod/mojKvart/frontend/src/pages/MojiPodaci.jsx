import { Navbar } from "../components/Navbar";
import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";


export function MojiPodaci(){
    const navigate = useNavigate();
    const [firstName, setFirstName] = useState('')
   const [lastName, setLastName] = useState('')
   const [homeAddress, setHomeAddress] = useState('')
   const [emailAddress, setEmailAddress] = useState('')
   const [id,setId] = useState(null)
   const [sifra, setSifra] = useState('')
    const [losunos,setlosunos] = useState(false);
    const [errorMessage, setErrorMessage] = useState('');
   //console.log(localStorage);
   
    //dohvacanje ID-a
    useEffect(() => {
        const storedId = localStorage.getItem('id');
        if (storedId) {
            setId(storedId);
            console.log(id)
        }
    }, []); //ovdje mislim da prazno jer se ovo radi samo jednom na pocetku

    //dohvcanje podataka preko id-a
    useEffect(() =>{
        const token = localStorage.getItem('token');
        const options = {
            method: 'GET',
            headers: {
              'Authorization': `Bearer ${token}`,
              'Content-Type': 'application/json'
            }
        }
        
        if(id){
            fetch(`/api/kupacs/${id}`,options)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                setFirstName(data.kupacIme);
                setLastName(data.kupacPrezime);
                setHomeAddress(data.kupacAdresa);
                setEmailAddress(data.kupacEmail);
                setSifra(data.kupacSifra)
            })
            .catch(error => {
                console.error('There was a problem with the fetch operation:', error);
            });
        }
    }, [id] //ovdje mislim da treba id
    
    )

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
                kupacid : id,
                kupacSifra:sifra

            })
        }
        fetch(`/api/kupacs/${id}`, options)
        .then(response => response.ok ? response.json() : Promise.reject('Failed to save changes'))
        .then(updatedId => {
            console.log('Uspješno ažurirano za kupac ID:', updatedId);
            alert("Promjene unesene!")
        })
        .catch(error => console.error('Error updating data:', error));
   }

   function handleClose(){
        navigate('/korisnickiracun');
        return;
   }

    return(
        <div>
            <Navbar/>
            <div className="main-container">
                    <div className="osobni-podaci-container">
                        <h1 className="naslov">Osobni podaci:</h1>
                        <form id="profile-form" onSubmit = {savePromjene}>
                            <div className="form-group">
                                <label >Ime:</label>
                                <input type="text"  placeholder={firstName} className="signup-inputs" name="firstName" value={firstName}
                                onChange={(e) => setFirstName(e.target.value)} />
                            </div>
                            
                            <div className="form-group">
                                <label >Prezime:</label>
                                <input type="text"  placeholder={lastName} className="signup-inputs" name="lastname" value={lastName}
                                onChange={(e) => setLastName(e.target.value)} />
                            </div>
                            
                            <div className="form-group">
                                <label >Email:</label>
                                <input type="text"  placeholder={emailAddress} className="signup-inputs" name="emailAddress" value={emailAddress}
                                onChange={(e) => setEmailAddress(e.target.value) } readOnly />
                            </div>
                            
                            <div className="form-group">
                                <label >Adresa:</label>
                                <input type="text"  placeholder={homeAddress} className="signup-inputs" name="homeAddress" value={homeAddress}
                                onChange={(e) => setHomeAddress(e.target.value)} />
                            </div>
                            {/* Prikaz poruke o grešci ako postoji */}
                            {errorMessage && <p style={{ color: 'red' }}>{errorMessage}</p>}
                            
                            <button type="submit" id="submit-button">Spremi promjene</button> 
                            <button 
                                type="button" 
                                onClick={handleClose} 
                                disabled={!!errorMessage}  // Ako errorMessage postoji, gumb je disabled
                            >
                                Zatvori
                            </button>
                        </form>

                    </div>
                </div>
        </div>
    )
    
}