import React, { useEffect, useState } from 'react';

function App() {
    const [message, setMessage] = useState("Backend'e bağlanılıyor...");
    const [page, setPage] = useState("home"); // Basit bir sayfa yönlendirmesi (Router kullanmadan)

    // Backend'den veri çekme (Integration Kanıtı)
    useEffect(() => {
        // Docker ortamında localhost:8080'e istek atacağız
        const apiUrl = process.env.REACT_APP_API_URL || "http://localhost:8080";

        fetch(`${apiUrl}/auth/hello`) // Backend'de böyle bir endpoint olduğunu varsayıyoruz
            .then(res => {
                if(res.ok) return res.text();
                throw new Error("Bağlantı Hatası");
            })
            .then(data => setMessage(data))
            .catch(err => setMessage("Backend ile iletişim kurulamadı: " + err.message));
    }, []);

    // Basit Sayfa Yönlendirmesi (Testler için)
    const renderContent = () => {
        if (page === "hello") {
            return (
                <div id="hello-page">
                    <h2>Hello Page</h2>
                    <p id="greeting">{message}</p>
                    <button onClick={() => setPage("home")}>Geri Dön</button>
                </div>
            );
        }
        return (
            <div>
                <h1>Demo App</h1>
                <p>Yazılım Doğrulama ve Geçerleme Projesi</p>
                <p>Sistem Durumu: {message}</p>
                {/* AŞAĞIDAKİ SATIRA id="hello-btn" EKLEDİM */}
                <button id="hello-btn" onClick={() => setPage("hello")}>Hello Sayfasına Git</button>
            </div>
        );
    };

    return (
        <div className="App" style={{ padding: "20px", fontFamily: "Arial" }}>
            {renderContent()}
        </div>
    );
}

export default App;