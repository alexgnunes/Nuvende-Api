import { createBrowserRouter } from 'react-router-dom';
import Login from '../pages/Login';
import Pix from '../pages/Pix';
import { PrivateTemplate } from '../components/PrivateTemplate';

export const routes = createBrowserRouter([
  {
    path: '/',
    element: <Login />,
  },
  {
    path: '/pix',
    element: <PrivateTemplate />,
    children: [
      {
        path: '/pix',
        element: <Pix />,
      },
    ],
  },
  {
    path: '*',
    element: (
      <div>
        <h1>Página não encontrada</h1>
      </div>
    ),
  },
]);