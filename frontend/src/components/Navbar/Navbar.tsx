import React, { useEffect, useState } from 'react';
import logo from "../../assets/logo.svg";
import avatar from "../../assets/avatar.jpg";
import { Link, NavigateFunction, useNavigate } from 'react-router-dom';
import { Button } from "@/components/ui/button";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuGroup,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuPortal,
  DropdownMenuSeparator,
  DropdownMenuShortcut,
  DropdownMenuSub,
  DropdownMenuSubContent,
  DropdownMenuSubTrigger,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { useAuth } from '@/hooks/UseAuth';

interface Props {
  children?: React.ReactNode;
  isAuthenticated: boolean;
  routeName?: string;
}

function MyDiscsLink(routeName?: string) {
  return (
    <Link to={'/myDiscs'}
      className={`
                text-[#FCFCFC] text-base min-w-fit text-nowrap
                ${routeName === '/myDiscs' ? 'font-extrabold' : 'font-medium'
        }`}>
      Meus Discos
    </Link>
  )
}

function MyWalletLink(routeName?: string) {
  return (
    <Link to={'/myWallet'}
      className={`
      text-[#FCFCFC] text-base min-w-fit text-nowrap
        ${routeName === '/myWallet' ? 'font-extrabold' : 'font-medium'
        }`}>
      Carteiras
    </Link>
  )
}

function Start(_navigate: NavigateFunction) {
  return (
    <div className="justify-start items-center flex">
      <Link to={'/home'} className="flex flex-row items-center">
        <img src={logo} className="h-9 sm:h-10" />
        <span className="font-comic ml-2 sm:ml-4 md:ml-6 font-bold text-lg sm:text-xl leading-none tracking-tight text-center">BootPlay</span>
      </Link>
    </div>
  )
}

function Center(_navigate: NavigateFunction, children: React.ReactNode) {
  return (
    <div className="justify-center items-center h-4/5 w-4/12 flex space-x-10">
      {children}
    </div>
  )
}

function End(_navigate: NavigateFunction, isAuthenticated: boolean, routeName?: string) {
  const [width, setWidth] = useState(window.innerWidth);
  const { logout } = useAuth();

  useEffect(() => {
    const handleResize = () => {
      setWidth(window.innerWidth);
    };

    window.addEventListener('resize', handleResize);

    return () => {
      window.removeEventListener('resize', handleResize);
    };
  }, []);

  return (
    <div className={`
      justify-end items-center 
      grid grid-cols-1 ${isAuthenticated ? 'sm:grid-cols-3' : 'sm:grid-cols-2'} 
      space-y-2 sm:space-y-0 
      space-x-0 sm:space-x-6 md:space-x-9 lg:space-x-10 xl:space-x-11 2xl:space-x-12
    `}>
      {isAuthenticated ?
        <>
          {(width > 768) ? (
            <>
              {MyDiscsLink(routeName)}
              {MyWalletLink(routeName)}
            </>
          ) : (
            <>
              {/* Why empty? To create space for smaller screens */}
            </>)
          }
          <DropdownMenu>
            <DropdownMenuTrigger asChild>
              <Button variant="ghost">
                <img src={avatar}
                  className={` min-w-fit h-10 rounded-full ring-4 hover:ring-blue-400`} >
                </img>
              </Button>
            </DropdownMenuTrigger>
            <DropdownMenuContent className="w-56 bg-slate-700">
              <DropdownMenuLabel className="text-[#FCFCFC] text-base text-center">Navegação</DropdownMenuLabel>
              <DropdownMenuSeparator className="bg-white" />
              <DropdownMenuGroup>
                <DropdownMenuItem>
                  <Link to={'/home'}
                    className={`
                          text-[#FCFCFC] text-base min-w-fit text-nowrap
                          ${routeName === '/home' ? 'font-extrabold' : 'font-medium'
                      }`}>
                    Comprar Discos
                  </Link>
                </DropdownMenuItem>
                <DropdownMenuItem>
                  {MyDiscsLink(routeName)}
                </DropdownMenuItem>
                <DropdownMenuItem>
                  {MyWalletLink(routeName)}
                </DropdownMenuItem>
                <DropdownMenuItem>
                  <Button variant="default"
                    onClick={logout}
                    className="p-0 m-0 text-[#FCFCFC] text-base min-w-fit text-nowrap max-h-[1.5rem]">
                    Deslogar
                  </Button>
                </DropdownMenuItem>
              </DropdownMenuGroup>
            </DropdownMenuContent>
          </DropdownMenu>
        </>
        :
        <>
          <button onClick={() => _navigate('/login')}
            className="bg-sysmap_dark w-24 md:w-44 h-8 sm:h-10 rounded-[32px] text-white"
          >Entrar</button>
          <button onClick={() => _navigate('/signup')}
            className="bg-sysmap_ligth w-24 md:w-44 h-8 sm:h-10 rounded-[32px] text-sysmap_dark"
          >Inscrever-se</button>
        </>
      }
    </div >
  )
}

export default function Navbar({ children, isAuthenticated, routeName }: Props) {
  const _navigate = useNavigate();

  return (
    <nav className="w-full h-20 bg-slate-100/20 sticky top-0 z-50 backdrop-filter backdrop-blur-[10px]">
      <div className="container text-white h-20 flex justify-between items-center sm:space-x-5">
        {Start(_navigate)}
        {Center(_navigate, children)}
        {End(_navigate, isAuthenticated, routeName)}
      </div >
    </nav >
  )
}
