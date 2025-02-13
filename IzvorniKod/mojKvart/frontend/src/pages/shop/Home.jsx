import React, { useEffect, useState } from "react";
import { Navbar } from '../../components/ShopNavbar';
import "../../styles/Home.css";
import "bootstrap/dist/css/bootstrap.min.css";

export function ShopHome() {
    const [shopEmail, setShopEmail] = useState(null);
    const [shopId, setShopId] = useState(null);
    const [productsType, setProductsType] = useState("approved");
    const [toUpdate, setToUpdate] = useState(true);
    const [error, setError] = useState("");
    const [popupError, setPopupError] = useState("Proizvod će biti poslan na odobravanje!");
    const [products, setProducts] = useState([]);
    const [productData, setProductData] = useState({
        "proizvodId": "placeholder",
        "proizvodNaziv": "",
        "proizvodOpis": "",
        "proizvodCijena": "",
        "proizvodKategorija": "",
        "proizvodSlika": "",
        "proizvodFlag": "N",
        "trgovina": -1   
    });
  
    useEffect(() => {
        const token = localStorage.getItem('token');
        const options = {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
            },
            body: JSON.stringify({ "oneLiner": token }),
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
                setShopEmail(data.email);
            })
            .catch(error => setError(error.message));
    }, []);

    useEffect(() => {
        if (!shopEmail) return;

        const token = localStorage.getItem('token');
        const options = {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        }
        
        fetch(`/api/trgovinas/${shopEmail}`, options)
            .then(async (response) => {
                if (!response.ok) {
                    const text = await response.text();
                    throw new Error(text);
                }
                return response.json();
            })
            .then((data) => {
                setShopId(data.trgovinaId);
            })
            .catch((error) => {
                setError(error.message);
            });
    }, [shopEmail]);

    useEffect(() => {
        if (!shopId) return;

        const token = localStorage.getItem('token');
        const options = {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        }

        fetch(`/api/proizvods/${productsType}/${shopId}`, options)
            .then(async (response) => {
                if (!response.ok) {
                    const text = await response.text();
                    throw new Error(text);
                }
                return response.json();
            })
            .then((data) => {
                setProducts(data);
            })
            .catch((error) => {
                setError(error.message);
            });
    }, [shopId, productsType]);

    const handleProductsTypeChange = (e) => {
        setProductsType(e.target.value);
    };

    const handleProductChange = (e) => {
        const { name, value } = e.target;
        setProductData((prevData) => ({ ...prevData, [name]: value }));
    };

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

    function showCreateForm() {
        setToUpdate(false);
        const element = document.getElementById("vani2");
        element.style.cursor = "not-allowed";
        element.style.opacity = 0.5;
        document.getElementById("registrationPopup").style.display = "flex";
    }

    function showUpdateForm(product) {
        product.proizvodFlag = "N"; 
        setProductData(product);
        const element = document.getElementById("vani2");
        element.style.cursor = "not-allowed";
        element.style.opacity = 0.5;
        document.getElementById("registrationPopup").style.display = "flex";
    }

    function closeProductForm() {
        const element = document.getElementById("vani2");
        element.style.cursor = "auto";
        element.style.opacity = 1;
        document.getElementById("registrationPopup").style.display = "none";
        setProductData({
            "proizvodId": "placeholder",
            "proizvodNaziv": "",
            "proizvodOpis": "",
            "proizvodCijena": "",
            "proizvodKategorija": "",
            "proizvodSlika": "",
            "proizvodFlag": "N",
            "trgovina": -1   
        });
        setToUpdate(true);
    }

    function createProduct() {
        if(isAnyPropertyEmpty(productData)) {
            setPopupError("Sva polja moraju biti popunjena!");
            return;
        }
        
        productData.proizvodId = null;
        productData.trgovina = shopId;
        productData.proizvodCijena = +(productData.proizvodCijena);
        
        const token = localStorage.getItem('token');
        const options = {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(productData)
        }

        fetch("/api/proizvods", options)
            .then(async (response) => {
                if (!response.ok) {
                    const text = await response.text();
                    throw new Error(text);
                }
            })
            .then(data => {
                closeProductForm();
                window.location.reload();
            })
            .catch((error) => {
                setPopupError(error.message);
            });
    }

    function updateProduct() {
        if(isAnyPropertyEmpty(productData)) {
            setPopupError("Sva polja moraju biti popunjena!");
            return;
        }

        const token = localStorage.getItem('token');
        const options = {
            method: 'PUT',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(productData)
        }

        fetch(`/api/proizvods/${productData.proizvodId}`, options)
            .then(async (response) => {
                if (!response.ok) {
                    const text = await response.text();
                    throw new Error(text);
                }
            })
            .then(data => {
                closeProductForm();
                window.location.reload();
            })
            .catch((error) => {
                setError(error.message);
            });
    }


    return (
        <div>
            <div id="vani2" style={{"minHeight": "100vh"}}>
                <Navbar />
                {/* {error && <p>{error}</p>} */}
                <div id="products-alt" className="product-section-alt">
                    <div id="productController">
                        <label>
                            <input
                            type="radio"
                            name="productsType"
                            value="approved"
                            checked={productsType === "approved"}
                            onChange={handleProductsTypeChange}
                            />
                            Odobreni proizvodi
                        </label>
                        <label>
                            <input
                            type="radio"
                            name="productsType"
                            value="notApproved"
                            checked={productsType === "notApproved"}
                            onChange={handleProductsTypeChange}
                            />
                            Proizvodi koji čekaju odobrenje
                        </label>
                        <label>
                            <input
                            type="radio"
                            name="productsType"
                            value="rejected"
                            checked={productsType === "rejected"}
                            onChange={handleProductsTypeChange}
                            />
                            Neodobreni proizvodi
                        </label>
                        <button id="addProduct" onClick={showCreateForm}>Dodaj proizvod</button>
                    </div>
                    <div className="row">
                        {products.length > 0 ? (
                            products.map((product) => (
                                <div id="productsbox" key={product.proizvodId} className="col-lg-4 col-md-6 col-12 mb-3-alt">
                                    <div className="card-alt product-card-alt">
                                        <img 
                                        src={product.proizvodSlika} 
                                        className="card-img-top-alt" 
                                        alt={product.proizvod_naziv} 
                                        />
                                        <div className="card-body-alt">
                                            <h5 className="card-title-alt">{product.proizvodNaziv}</h5>
                                            <p className="card-text-alt">{product.proizvodOpis}</p>
                                            <div id="gumbcijena-alt">
                                                <p className="price-alt">€{product.proizvodCijena}</p>
                                                <button className="add-to-cart-btn-alt" onClick={() => showUpdateForm(product)}>Ažuriraj proizvod</button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            ))
                        ) : (
                            <p>Nema proizvoda u ovoj kategoriji.</p>
                        )}
                    </div>
                </div>
            </div>
            <div id="registrationPopup">
                <input
                    type="text"
                    placeholder="Naziv proizvoda"
                    className="proizvod-inputs"
                    name="proizvodNaziv"
                    value={productData.proizvodNaziv}
                    onChange={handleProductChange}
                    maxLength={50}
                />
                <textarea
                    type="text"
                    placeholder="Opis proizvoda"
                    className="proizvod-inputs"
                    name="proizvodOpis"
                    value={productData.proizvodOpis}
                    onChange={handleProductChange}
                    maxLength={200}
                />
                <input
                    type="number"
                    placeholder="Cijena proizvoda u €"
                    className="proizvod-inputs"
                    name="proizvodCijena"
                    value={productData.proizvodCijena}
                    onChange={handleProductChange}
                    step="0.01"
                />
                <input
                    type="text"
                    placeholder="Kategorija proizvoda"
                    className="proizvod-inputs"
                    name="proizvodKategorija"
                    value={productData.proizvodKategorija}
                    onChange={handleProductChange}
                    maxLength={50}
                />
                <input
                    type="text"
                    placeholder="Slika proizvoda (URL)"
                    className="proizvod-inputs"
                    name="proizvodSlika"
                    value={productData.proizvodSlika}
                    onChange={handleProductChange}
                    maxLength={200}
                />
                {popupError && <p style={{"textAlign": "center", "color": "red"}}>{popupError}</p>}
                {!toUpdate && <div className="YesNoButtons">
                    <button onClick={createProduct}>Spremi proizvod</button>
                    <button onClick={closeProductForm}>Odustani</button>
                </div>}
                {toUpdate && <div className="YesNoButtons">
                    <button onClick={updateProduct}>Ažuriraj proizvod</button>
                    <button onClick={closeProductForm}>Odustani</button>
                </div>}
            </div>
        </div>
    );
}