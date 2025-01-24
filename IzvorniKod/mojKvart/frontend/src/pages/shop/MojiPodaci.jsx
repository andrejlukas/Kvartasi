import { Navbar } from "../../components/ShopNavbar";
import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { MapContainer, TileLayer, Marker, useMapEvents } from 'react-leaflet';
import "leaflet/dist/leaflet.css";
import "../../styles/MojiPodaci.css";

export function ShopMojiPodaci(){
    const navigate = useNavigate();
    const [emailAddress, setEmailAddress] = useState("");
    const [shopData, setShopData] = useState({
        trgovinaId: null,
        trgovinaNaziv: "",
        trgovinaOpis: "",
        trgovinaKategorija: "",
        trgovinaLokacija: "",
        trgovinaSlika: "",
        trgovinaRadnoVrijemeOd: "",
        trgovinaRadnoVrijemeDo: "",
        trgovinaEmail: emailAddress,
        trgovinaSifra: "",
        imaAtributeAtributs: []
    });

    const [errorMessage, setErrorMessage] = useState("");
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
    
        if(emailAddress) {
            fetch(`/api/trgovinas/${emailAddress}`, options)
                .then(async response => {
                    if (!response.ok) {
                        const text = await response.text();
                        throw new Error(text);
                    }
                    return response.json();
                })
                .then(data => { setShopData(data) })
                .catch(error => setErrorMessage(error.message));
        }
    }, [emailAddress]);

    useEffect(() => {
        const initialPosition = getCurrentLocation();
        setMarkerPosition(initialPosition);
    }, [shopData.trgovinaLokacija]);

    function savePromjene(e){
        e.preventDefault();
        
        if(isAnyPropertyEmpty(shopData)) {
            setErrorMessage("Sva polja moraju biti popunjena.");
            return;
        } else setErrorMessage('');

        const token = localStorage.getItem('token');
        const options ={
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(shopData)
        }
        
        fetch(`/api/trgovinas/${shopData.trgovinaId}`, options)
        .then(response => response.ok ? response.json() : response.text().then(text => {throw new Error(text)}))
        .then(updated => {
            setHasChanges(false);
        })
        .catch(error => setErrorMessage(error.message))

        navigate('/racuntrgovine');
    }

    function isAnyPropertyEmpty(data) {
        for (const key in data) {
            if (data.hasOwnProperty(key)) {
                const value = data[key];
                if (value === null || value === "") {
                    return true;
                }
            }
        }
        return false;
    }

    function handleClose() {
        navigate('/racuntrgovine');
        return;
    }

    const LocationMarker = () => {
        useMapEvents({
            click(e) {
                const { lat, lng } = e.latlng;
                setMarkerPosition([lat, lng]);
                setShopData((prevData) => ({ ...prevData, ["trgovinaLokacija"]: `${lat},${lng}` }));
                setHasChanges(true);
            },
        });

        return markerPosition ? <Marker position={markerPosition} /> : null;
    };

    const handleShopChange = (e) => {
        const { name, value } = e.target;
        setShopData((prevData) => ({ ...prevData, [name]: value }));
        setHasChanges(true); 
    };

    const getCurrentLocation = () => {
        if (shopData.trgovinaLokacija) {
            const [lat, lng] = shopData.trgovinaLokacija.split(',').map(Number);
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
                        <h1 className="naslov">Podaci trgovine:</h1>
                        <form id="moji-podaci-profile-form" onSubmit = {savePromjene}>
                            <div className="moji-podaci-form-group">
                                <label>Naziv trgovine:</label>
                                <input
                                    className="moji-podaci-inputs"
                                    type="text"
                                    placeholder="Naziv trgovine"
                                    name="trgovinaNaziv"
                                    value={shopData.trgovinaNaziv}
                                    onChange={handleShopChange}
                                />
                            </div>
                            
                            <div className="moji-podaci-form-group">
                                <label>Opis trgovine:</label>
                                <textarea
                                    placeholder="Opis trgovine"
                                    className="moji-podaci-inputs"
                                    name="trgovinaOpis"
                                    value={shopData.trgovinaOpis}
                                    onChange={handleShopChange}
                                />
                            </div>
                            
                            <div className="moji-podaci-form-group">
                                <label>Kategorija trgovine:</label>
                                <input
                                    type="text"
                                    placeholder="Kategorija trgovine"
                                    className="moji-podaci-inputs"
                                    name="trgovinaKategorija"
                                    value={shopData.trgovinaKategorija}
                                    onChange={handleShopChange}
                                />
                            </div>
                            
                            <div className="moji-podaci-form-group">
                                <label>URL slike trgovine:</label>
                                <input
                                    type="text"
                                    placeholder="Slika trgovine (URL)"
                                    className="moji-podaci-inputs"
                                    name="trgovinaSlika"
                                    value={shopData.trgovinaSlika}
                                    onChange={handleShopChange}
                                />
                            </div>
                            
                            <div className="moji-podaci-form-group">
                                <label>Radno vrijeme trgovine:</label>
                                <div className="moji-podaci-inputs">
                                    <div className="timestamp">
                                        <span>Od: </span>
                                        <input
                                            type="time"
                                            placeholder="Radno vrijeme od"
                                            name="trgovinaRadnoVrijemeOd"
                                            value={shopData.trgovinaRadnoVrijemeOd}
                                            onChange={handleShopChange}
                                        />
                                    </div>
                                    <div className="timestamp">
                                        <span>Do: </span>
                                        <input
                                            type="time"
                                            placeholder="Radno vrijeme do"
                                            name="trgovinaRadnoVrijemeDo"
                                            value={shopData.trgovinaRadnoVrijemeDo}
                                            onChange={handleShopChange}
                                        />
                                    </div>
                                </div>
                            </div>
                            
                            <div className="moji-podaci-form-group">
                                <label>Email trgovine:</label>
                                <input
                                    type="email"
                                    placeholder="E-mail trgovine"
                                    className="moji-podaci-inputs"
                                    name="trgovinaEmail"
                                    value={shopData.trgovinaEmail}
                                    onChange={handleShopChange}
                                />
                            </div>
                            
                            <div className="moji-podaci-form-group">
                                <label>Lokacija trgovine</label>
                                <div id="leafletMapShop" className="moji-podaci-inputs">
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
                            </div>

                            {errorMessage && <p style={{ color: "red", display: "flex", justifyContent: "center" }}>{errorMessage}</p>}
                            
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