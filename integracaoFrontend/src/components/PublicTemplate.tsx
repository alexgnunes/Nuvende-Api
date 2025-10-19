import { Container, CssBaseline } from '@mui/material';

export function PublicTemplate({ children }: { children: React.ReactNode }) {
  return (
    <>
      <CssBaseline />
      <Container maxWidth="sm" sx={{ mt: 4, mb: 4 }}>
        {children}
      </Container>
    </>
  );
}