import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { TextField, Typography, Button } from '@mui/material';
import styles from '../styles/style.module.css';
import { PublicTemplate } from '../components/PublicTemplate';

const Login: React.FC = () => {
  const [clientId, setClientId] = useState('');
  const [clientSecret, setClientSecret] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');

    try {
      const response = await axios.post('http://localhost:8080/auth/login', {
        clientId,
        clientSecret,
      }, {
        headers: { 'Content-Type': 'application/json' },
      });
      const token = response.data.accessToken;
      if (!token) {
        throw new Error('Token não retornado pela API');
      }
      localStorage.setItem('token', token);
      navigate('/pix');
    } catch (err) {
      setError('Erro ao fazer login. Verifique as credenciais.');
      console.error('Erro no login:', err);
    }
  };

  return (
    <>
      <h2>
        <title>Login</title>
        <meta name="description" content="Página de Login do App de Pagamento" />
      </h2>
      <PublicTemplate>
        <div className={styles.container}>
          <Typography variant="h4" fontWeight={500} color="#000000ff" gutterBottom>
            Efetue seu login
          </Typography>
          <Typography variant="body2" fontStyle="italic" gutterBottom>
            Para acessar o sistema de pagamento PIX
          </Typography>
          <form className={styles.form} onSubmit={handleSubmit}>
            <TextField
              value={clientId}
              onChange={(e) => setClientId(e.target.value)}
              label="Client ID"
              variant="outlined"
              color="error"
              fullWidth
              required
            />
            <TextField
              value={clientSecret}
              onChange={(e) => setClientSecret(e.target.value)}
              label="Client Secret"
              type="password"
              variant="outlined"
              color="error"
              fullWidth
              required
            />
            {error && <Typography color="error" sx={{ mt: 1 }}>{error}</Typography>}
            <Button type="submit" variant="contained" color="primary" fullWidth sx={{ mt: 2 }}>
              Entrar
            </Button>
          </form>
        </div>
      </PublicTemplate>
    </>
  );
};

export default Login;