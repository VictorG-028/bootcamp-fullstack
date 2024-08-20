import { FormEvent, useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom';
import toast from 'react-hot-toast';
import { useAuth } from '@/hooks/UseAuth';
import Input from '@/components/Input';
import { Button } from '@/components/ui/button';
import { Loader2 } from 'lucide-react';
import cross from '../../assets/cross.svg';
import logo from '../../assets/logo.svg';

export default function Login() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);
  const { login, isAuthenticated } = useAuth();
  const _navigate = useNavigate();

  useEffect(() => {
    if (isAuthenticated) {
      _navigate('/home');
    }
  }, [isAuthenticated]);

  async function handleLogin(event: FormEvent) {
    event.preventDefault();
    setLoading(true);

    await login(email, password)
      .then(() => {
        toast.success(`Sucesso ao logar conta!`);
      }).catch(() => {
        toast.error("Erro ao efetuar login! [catch]");
        setLoading(false);
      });
  }

  return (
    <>
      {isAuthenticated && _navigate('/home')}
      <main className="bg-fundo bg-cover bg-no-repeat h-screen">
        <div className="flex items-center justify-center h-screen backdrop-brightness-50 backdrop-blur-sm">

          <div className="flex max-w-[544px] bg-white p-10 rounded-md">
            <div className="flex flex-col items-center w-full gap-2">
              <button onClick={() => _navigate('/')}>
                <img src={cross} className='relative top-[-20px] right-[-140px] h-8'></img>
              </button>
              <img src={logo} className="h-12" />
              <h1 className="text-3xl font-semibold">Acesse sua conta</h1>
              <form onSubmit={handleLogin} className="flex flex-col w-72 gap-2">
                <Input onChange={e => setEmail(e.target.value)} type='email'>Email</Input>
                <Input onChange={e => setPassword(e.target.value)} type='password'>Senha</Input>
                {loading ?
                  <Button disabled>
                    <Loader2 className="h-12 animate-spin" />
                    Carregando...
                  </Button>
                  :
                  <Button type='submit' disabled={false} className="p-3 h-12 font-medium text-white text-base bg-zinc-900 hover:bg-zinc-900/90 transition mb-3 rounded-full">
                    Entrar
                  </Button>
                }
              </form>
              <p className="text-base font-normal">Ainda não tem conta ? <a href="/signup" className="font-semibold underline">Inscrever-se</a></p>
            </div>
          </div>

        </div>
      </main>
    </>
  )
}
