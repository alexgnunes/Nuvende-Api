import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { QRCodeCanvas } from 'qrcode.react';
import { Typography, Button } from '@mui/material';
import styles from '../styles/style.module.css';
import { PublicTemplate } from '../components/PublicTemplate';

const Pix: React.FC = () => {
  const [qrCode, setQrCode] = useState('');
  const [status, setStatus] = useState('');
  const token = localStorage.getItem('token');

  useEffect(() => {
    if (!token) {
      return;
    }

    const fetchPix = async () => {
      try {
        const response = await axios.post(
          'http://localhost:8080/pix/create',
          {
            chave: '59ba4ca7-e1d4-433f-8dbf-77e692434a69',
            nomeRecebedor: 'DOUTBOX',
            nomeDevedor: 'Alex Nunes',
            valor: 10.00,
            cpf: '12345678900',
            cnpj: '',
            txid: '2135',
            solicitacaoPagador: 'Pagamento referente à compra',
          },
          {
            headers: { Authorization: `Bearer ${token}`, 'Content-Type': 'application/json' },
          }
        );
        setQrCode(response.data.qrCode);
        setStatus(response.data.status);
      } catch (err) {
        console.error('Erro no fetchPix:', err);
      }
    };

    fetchPix();
  }, [token]);

  const handleLogout = () => {
    localStorage.removeItem('token');
    window.location.href = '/';
  };

  return (
    <>
      <h2>
        <title>Pagamento PIX</title>
        <meta name="description" content="Página de Pagamento PIX" />
      </h2>
      <PublicTemplate>
        <div className={styles.container}>
          <Typography variant="h4" fontWeight={500} color="#000000ff" gutterBottom>
            Pagamento PIX
          </Typography>
          <Typography variant="body2" fontStyle="italic" gutterBottom>
            Escaneie o QR Code para pagar
          </Typography>
          <QRCodeCanvas value={qrCode} size={256} />
          <Typography variant="h6" color="primary" gutterBottom>
            Status: <strong>{status}</strong>
          </Typography>
          <Button
            variant="contained"
            color="primary"
            fullWidth
            onClick={handleLogout}
            sx={{ mt: 2, maxWidth: '200px', margin: '0 auto' }}
          >
            Sair
          </Button>
        </div>
      </PublicTemplate>
    </>
  );
};

export default Pix;