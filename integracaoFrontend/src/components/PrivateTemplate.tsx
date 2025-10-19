import React from 'react';
import { Outlet, useNavigate } from 'react-router-dom';
import { Container, CssBaseline } from '@mui/material';

export const PrivateTemplate: React.FC = () => {
  const navigate = useNavigate();
  const token = localStorage.getItem('token');

  React.useEffect(() => {
    if (!token) {
      navigate('/');
    }
  }, [token, navigate]);

  if (!token) {
    return null;
  }

  return (
    <>
      <CssBaseline />
      <Container maxWidth="sm" sx={{ mt: 4, mb: 4 }}>
        <Outlet />
      </Container>
    </>
  );
};