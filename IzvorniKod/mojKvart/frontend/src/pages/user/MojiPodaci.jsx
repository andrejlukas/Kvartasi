import { Navbar } from "../../components/Navbar";
import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { MapContainer, TileLayer, Marker, useMapEvents  } from 'react-leaflet';
import "leaflet/dist/leaflet.css";
import "../../styles/MojiPodaci.css";

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
    const [markerPosition, setMarkerPosition] = useState(null);
   
    useEffect(() => {
        const token = localStorage.getItem('token');
        const options = {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ oneLiner: token })
        }

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
            fetch(`/api/kupacs/${emailAddress}`, options)
                .then(response => {
                    if (!response.ok) {
                        const text = response.text();
                        throw new Error(text);
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
                .catch(error => setErrorMessage(error.message));
        }
    }, [emailAddress]);

   function savePromjene(e){
        e.preventDefault();

        if (!firstName || !lastName || !homeAddress || !emailAddress) {
            setErrorMessage("Sva polja moraju biti popunjena.");
            return;
        }

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
                kupacSifra:sifra,
                kupacStatus:"V"
            })
        }
        
        fetch(`/api/kupacs/${id}`, options)
        .then(response => response.ok ? response.json() : Promise.reject('Failed to save changes'))
        .then(updated => {
            setHasChanges(false);
        })
        .catch(error => {
            console.error('Error updating data:', error);
        })

        navigate('/korisnickiracun');
   }

   function handleClose() {
        navigate('/korisnickiracun');
        return;
   }

   function handleInputChange(setter, value) {
    setter(value);
    setHasChanges(true); 
   }

   const LocationMarker = () => {
        useMapEvents({
            click(e) {
                const { lat, lng } = e.latlng;
                setMarkerPosition([lat, lng]);
                setHomeAddress(`${lat},${lng}`);
                setHasChanges(true);
            },
        });

        return markerPosition ? <Marker position={markerPosition} /> : null;
    };

    useEffect(() => {
        const initialPosition = getCurrentLocation();
        setMarkerPosition(initialPosition);
    }, [homeAddress]);

    const getCurrentLocation = () => {
        if (homeAddress) {
            const [lat, lng] = homeAddress.split(',').map(Number);
            if (!isNaN(lat) && !isNaN(lng)) {
                return [lat, lng];
            }
        }
        return [45.815, 15.9819]; // default Zagreb
    };

    return(
        <div className="moji-podaci-wrapper">
            <Navbar/>
            <div className="main-container">
                    <div className="osobni-podaci-container">
                        <h1 className="naslov">Osobni podaci:</h1>
                        <form id="moji-podaci-profile-form" onSubmit = {savePromjene}>
                            <div className="moji-podaci-form-group">
                                <label >Ime:</label>
                                <input type="text"  placeholder={firstName} className="moji-podaci-inputs" name="firstName" value={firstName}
                                onChange={(e) => handleInputChange(setFirstName, e.target.value)} />
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
                                <label>Adresa:</label>
                                <MapContainer
                                    center={markerPosition || [45.815, 15.9819]}
                                    zoom={12}
                                    style={{ height: "400px", width: "100%" }}
                                    key={markerPosition}
                                >
                                    <TileLayer
                                        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                                        attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
                                    />
                                    <LocationMarker />
                                </MapContainer>
                            </div>

                            {errorMessage && <p style={{ color: 'red' }}>{errorMessage}</p>}
                            <div className="moji-podaci-gumbovi-container">
                                <button type="submit" id="spremi-promjene-button" disabled={ !hasChanges }>Spremi promjene</button> 
                                <button 
                                    type="button" 
                                    id = "zatvori-button"
                                    onClick={handleClose} 
                                    disabled={ errorMessage || hasChanges }>Zatvori</button>
                            </div>
                        </form>
                    </div>
                </div>
        </div>
    )
}