import React, { useEffect, useState } from "react";
import { Navbar } from '../../components/ModeratorNavbar';
import "../../styles/Home.css";
import "../../styles/PonudeiPromocije.css";
import "bootstrap/dist/css/bootstrap.min.css";
import QRCode from "qrcode";

export function ModeratorPonude() {
    // popustType je true ako je potvrden, a false ako nije 
    const [popustType, setPopustType] = useState(true);
    const [error, setError] = useState("");

    const [approvedPopust, setApprovedPopust] = useState([]);
    const [notApprovedPopust, setNotApprovedPopust] = useState([]);
    const [popusti, setPopust] = useState([]);

    const [approvedPonuda, setApprovedPonuda] = useState([]);
    const [notApprovedPonuda, setNotApprovedPonuda] = useState([]);
    const [ponude, setPonude] = useState([]);

    const [qrCodes, setQrCodes] = useState({});
    const [loading, setLoading] = useState(true);
    // const [ponudaPopustId, setPonudaPopustId] = useState();

    useEffect(() => {
        const token = localStorage.getItem('token');
        const options = {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        };

        fetch(`/api/popusts/flag-true`, options)
            .then(async (response) => {
                if (!response.ok) {
                    const text = await response.text();
                    throw new Error(text);
                }
                return response.json();
            })
            .then((data) => {
                setApprovedPopust(data);
                if (popustType === true) {
                    setPopust(data);
                    generateQRCodes(data); 
                }
            })
            .catch((error) => {
                setError(error.message);
            });

        fetch(`/api/popusts/flag-false`, options)
            .then(async (response) => {
                if (!response.ok) {
                    const text = await response.text();
                    throw new Error(text);
                }
                return response.json();
            })
            .then((data) => {
                setNotApprovedPopust(data);
                if (popustType === false) {
                    setPopust(data);
                    generateQRCodes(data); 
                }
            })
            .catch((error) => {
                setError(error.message);
            });
        
        
        fetch(`/api/ponudas/flag-true`, options)
            .then(async (response) => {
                if (!response.ok) {
                    const text = await response.text();
                    throw new Error(text);
                }
                return response.json();
            })
            .then((data) => {
                
                setApprovedPonuda(data);
                if (popustType === true) {
                    setPonude(data);
                }
            })
            .catch((error) => {
                setError(error.message);
            });
        
            fetch(`/api/ponudas/flag-false`, options)
            .then(async (response) => {
                if (!response.ok) {
                    const text = await response.text();
                    throw new Error(text);
                }
                return response.json();
            })
            .then((data) => {
                setNotApprovedPonuda(data);
                if (popustType === false) {
                    setPonude(data);
                }
            })
            .catch((error) => {
                setError(error.message);
            });


        setLoading(false);
    }, [popustType]);


    const OdobriPopust = async (popustData) => {
        try {
            const token = localStorage.getItem('token');
            const getOptions = {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json',
                },
            };
            // get ponudaPopust data
            const response = await fetch(`/api/ponudaPopusts/${popustData.ponudaPopust}`, getOptions);
            if (!response.ok) {
                const text = await response.text();
                throw new Error(text);
            }

            const data = await response.json();
            data.ponudaPopustFlag = true;

            const putOptions = {
                method: 'PUT',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(data),
            };
            //update ponudaPopust data
            const putResponse = await fetch(`/api/ponudaPopusts/${popustData.ponudaPopust}`, putOptions);
            if (!putResponse.ok) {
                const text = await putResponse.text();
                throw new Error(text);
            }

            setNotApprovedPopust((prevPopust) =>
                prevPopust.filter((popust) => popust.popustId !== popustData.popustId)
            );

            setApprovedPopust((prevPopust) => [...prevPopust, popustData]);
            setPopust((prevPopust) =>
                prevPopust.filter((popust) => popust.popustId !== popustData.popustId)
            );
        } catch (error) {
            setError(error.message);
        }
    };

    const OdbijPopust = (popustData) => {
        
    const token = localStorage.getItem('token');
    const options = {
        method: 'DELETE',
        headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        }
    };

    fetch(`/api/popusts/${popustData.popustId}`, options)
        .then(async (response) => {
            if (!response.ok) {
                const text = await response.text();
                throw new Error(text);
            }
            return response;
        })
        .then(() => {
            setNotApprovedPopust((prevPopust) =>
                prevPopust.filter((popust) => popust.popustId !== popustData.popustId)
            );

            setPopust((prevPopust) =>
                prevPopust.filter((popust) => popust.popustId !== popustData.popustId)
            );
        })
        .catch((error) => {
            setError(error.message);
        });
    };


    const OdobriPonuda = async (ponudaData) => {
        try {
            const token = localStorage.getItem('token');
            const getOptions = {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json',
                },
            };
            // get ponudaPopust data
            const response = await fetch(`/api/ponudaPopusts/${ponudaData.ponudaPopust}`, getOptions);
            if (!response.ok) {
                const text = await response.text();
                throw new Error(text);
            }

            const data = await response.json();
            data.ponudaPopustFlag = true;

            const putOptions = {
                method: 'PUT',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(data),
            };
            //update ponudaPopust data
            const putResponse = await fetch(`/api/ponudaPopusts/${ponudaData.ponudaPopust}`, putOptions);
            if (!putResponse.ok) {
                const text = await putResponse.text();
                throw new Error(text);
            }

            setNotApprovedPonuda((prevPonuda) =>
                prevPonuda.filter((ponuda) => ponuda.ponudaId !== ponudaData.ponudaId)
            );

            setApprovedPonuda((prevPonuda) => [...prevPonuda, ponudaData]);
            setPonude((prevPonuda) =>
                prevPonuda.filter((ponuda) => ponuda.ponudaId !== ponudaData.ponudaId)
            );
        } catch (error) {
            setError(error.message);
        }
    };


    const OdbijPonuda = (ponudaData) => {
        
    const token = localStorage.getItem('token');
    const options = {
        method: 'DELETE',
        headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        }
    };

    fetch(`/api/ponudas/${ponudaData.ponudaId}`, options)
        .then(async (response) => {
            if (!response.ok) {
                const text = await response.text();
                throw new Error(text);
            }
            return response;
        })
        .then(() => {
            setNotApprovedPonuda((prevPonuda) =>
                prevPonuda.filter((ponuda) => ponuda.ponudaId !== ponudaData.ponudaId)
            );

            setPonude((prevPonuda) =>
                prevPonuda.filter((ponuda) => ponuda.ponudaId !== ponudaData.ponudaId)
            );
        })
        .catch((error) => {
            setError(error.message);
        });
    };

    const handlePopustTypeChange = (e) => {
        const value = e.target.value === "true";
        setPopustType(value);
        if (value === true) {
            setPopust(approvedPopust);
            console.log("eeej"+approvedPonuda)
            setPonude(approvedPonuda);
            generateQRCodes(approvedPopust); 
        } else {
            setPopust(notApprovedPopust);
            setPonude(notApprovedPonuda);
            generateQRCodes(notApprovedPopust); 
        }
    };

    const generateQRCodes = async (data) => {
        const qrCodePromises = data.map(async (popust) => {
            const url = await QRCode.toDataURL(popust.popustQrkod);
            return { id: popust.popustId, url };
        });

        const qrCodeResults = await Promise.all(qrCodePromises);
        const qrCodeMap = qrCodeResults.reduce((acc, { id, url }) => {
            acc[id] = url;
            return acc;
        }, {});

        setQrCodes(qrCodeMap);
    };

    return (
        <div>
            <div id="vani2" style={{ minHeight: "100vh" }}>
                <Navbar />
                {error && <p>{error}</p>}
                <div id="products-alt" className="product-section-alt">
                    <div id="productController">
                        <label>
                            <input
                                type="radio"
                                name="popustiPonudeType"
                                value="true"
                                checked={popustType === true}
                                onChange={handlePopustTypeChange}
                            />
                            Odobreni popusti i ponude
                        </label>
                        <label>
                            <input
                                type="radio"
                                name="popustiPonudeType"
                                value="false"
                                checked={popustType === false}
                                onChange={handlePopustTypeChange}
                            />
                            Popusti i ponude koji čekaju odobrenje
                        </label>
                    </div>
                    <div className="container-popusti">
                        {loading ? (
                            <p>Loading...</p>
                        ) : popusti.length > 0 ? (
                            <div className="popust-popust-row">
                                <div className="banner">
                                    Pronađite najbolje ponude i popuste uz samo par klikova!
                                </div>
                                <div className="ponuda-popust-wrapper">
                                    <div id="popusti" className="popusti-section">
                                        {popusti.length > 0 && (
                                            popusti.map((pop) => (
                                                <div key={pop.popustId} className="my-popust-wrapper">
                                                    <div className="popust-card">
                                                        <div className="popust-header">
                                                            <p>{pop.popustNaziv} - {pop.trgovinaIme}</p>
                                                            <hr />
                                                        </div>
                                                        <div className="popust-body">
                                                            <div className="popust-info">
                                                                <p className="popust-details">{pop.popustOpis}</p>
                                                            </div>
                                                            <div className="popust-actions">
                                                                {qrCodes[pop.popustId] && (
                                                                    <img
                                                                        src={qrCodes[pop.popustId]}
                                                                        alt="QR Code"
                                                                        className="qr-code"
                                                                    />
                                                                )}
                                                                {popustType === false && (
                                                                    <>
                                                                        <button className="add-to-cart-btn-alt" onClick={() => OdobriPopust(pop)}>Odobri popust</button>
                                                                        <button className="add-to-cart-btn-alt" onClick={() => OdbijPopust(pop)}>Odbij popust</button>
                                                                    </>
                                                                )}
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            ))
                                        )}
                                    </div>
                                    <div id="ponude" className="popusti-section">
                                        {ponude.length > 0 && (
                                            ponude.map((ponuda) => (
                                                <div key={ponuda.ponudaId} className="my-popust-wrapper">
                                                    <div className="popust-card">
                                                        <div className="popust-header">
                                                            <p>{ponuda.ponudaNaziv} - {ponuda.trgovinaIme}</p>
                                                            <hr />
                                                        </div>
                                                        <div className="popust-body">
                                                            <div className="popust-info">
                                                                <p className="popust-details">{ponuda.ponudaOpis}</p>
                                                            </div>
                                                            <div className="popust-actions">
                                                                    {popustType === false && (
                                                                    <>
                                                                        <button className="add-to-cart-btn-alt" onClick={() => OdobriPonuda(ponuda)}>Odobri ponudu</button>
                                                                        <button className="add-to-cart-btn-alt" onClick={() => OdbijPonuda(ponuda)}>Odbij ponudu</button>
                                                                    </>
                                                                )}
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            ))
                                        )}
                                    </div>
                                </div>
                            </div>
                        ) : (
                            <p>Nema dostupnih popusta ili proizvoda</p>
                        )}
                    </div>
                </div>
            </div>
        </div>
    );
}