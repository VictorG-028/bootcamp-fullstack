import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '@/hooks/UseAuth';
import Navbar from '@/components/Navbar/Navbar';

export default function Landing() {
  const { isAuthenticated } = useAuth();
  const _navigate = useNavigate();

  useEffect(() => {
    if (isAuthenticated) {
      _navigate('/home');
    }
  }, []);


  return (
    <main className="bg-fundo bg-cover bg-no-repeat h-screen max-h-full w-full text-white flex flex-col">
      <div className="absolute inset-0 h-screen max-h-full bg-black opacity-50"></div>
      <Navbar isAuthenticated={isAuthenticated}> </Navbar>
      <div className="mx-auto mt-56 ml-24 space-y-4 backdrop-brightness-100">
        <h1 className="text-6xl font-semibold leading-96 text-left max-w-[600px] text-wrap whitespace-normal">A história da música não pode ser esquecida!</h1>
        <p className="text-2xl font-normal max-w-[400px] text-wrap">Crie já sua conta e curta os sucessos que marcaram os tempos no Vinil.</p>
        <button onClick={() => _navigate('/signup')} className="text-2xl bg-sysmap_ligth w-[269px] h-16 rounded-[32px] ml-4 text-sysmap_dark">Inscrever-se</button>
      </div>
    </main>
  );
}
