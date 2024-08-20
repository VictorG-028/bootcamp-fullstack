import { FormEvent, useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import toast from 'react-hot-toast';
import { useAuth } from '@/hooks/UseAuth';
import Input from '../../components/Input';
import { Button } from '@/components/ui/button';
import { Loader2 } from 'lucide-react';
import cross from '../../assets/cross.svg';
import logo from '../../assets/logo.svg';


export default function Signup() {

  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);
  const { signup, isAuthenticated } = useAuth();
  const _navigate = useNavigate();

  useEffect(() => {
    if (isAuthenticated) {
      _navigate('/home');
    }
  }, [isAuthenticated]);

  async function handleSigup(event: FormEvent) {
    event.preventDefault();
    setLoading(true);

    signup(name, email, password)
      .then(() => {
        toast.success(`Sucesso ao criar conta!`);
      }).catch(() => {
        toast.error("Erro ao efetuar login![catch]");
        setLoading(false);
      });
  }

  return (
    <main className="bg-fundo bg-cover bg-no-repeat h-screen">
      <div className="flex items-center justify-center h-screen backdrop-brightness-50 backdrop-blur-sm">

        <div className="flex max-w-[544px] bg-white p-10 rounded-md">
          <div className="flex flex-col items-center w-full gap-2">
            <button onClick={() => _navigate('/')}>
              <img src={cross} className='relative top-[-20px] right-[-140px] h-8'></img>
            </button>
            <img src={logo} className="h-12" />
            <h1 className="text-3xl font-semibold">Increva-se</h1>
            <form onSubmit={handleSigup} className="flex flex-col w-72 gap-2">
              <Input type='text' onChange={event => setName(event.target.value)}>Nome</Input>
              <Input type='email' required onChange={event => setEmail(event.target.value)}>Email</Input>
              <Input type='password' required onChange={event => setPassword(event.target.value)}>Senha</Input>

              {loading ?
                <Button disabled>
                  <Loader2 className="h-12 animate-spin" />
                  Carregando...
                </Button>
                :
                <Button type='submit' disabled={false} className="p-3 h-12 font-medium text-white text-base bg-zinc-900 hover:bg-zinc-900/90 transition mb-3 rounded-full">
                  Inscrever-se
                </Button>
              }
            </form>
            <p className="text-base font-normal">Já tem uma conta ? <a href="/login" className="font-semibold underline">Entrar</a></p>
          </div>
        </div>

      </div>
    </main>
  )
}
