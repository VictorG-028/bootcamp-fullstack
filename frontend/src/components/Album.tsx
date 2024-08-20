import React from 'react';
import { Card } from "../components/ui/card";
import { Disc3Icon } from 'lucide-react';

interface Props {
  key?: number;
  name: string;
  imageUrl: string;
  spotifyUrl?: string;
  price: number;
  backgroundInPrice?: boolean;
  isInsideCarousel?: boolean;
  animateOnHover?: boolean;
}

export default function Album({ key, name, imageUrl, spotifyUrl, price, backgroundInPrice, isInsideCarousel, animateOnHover }: Props) {

  return (
    <div className={`shadow-[#BDBDBD3B] shadow-lg blur-50 min-w-fit max-w-fit ${isInsideCarousel ? 'shadow-xl' : ''}`}>
      <Card key={key}
        style={{ '--bg-fundo': `url(${imageUrl})` } as React.CSSProperties}
        className={`
          border-0 bg-[image:var(--bg-fundo)] bg-cover bg-no-repeat 
          w-60 h-[245px] rounded-md 
          ${spotifyUrl ? 'cursor-pointer' : ''} 
          ${isInsideCarousel ? 'my-[1.0rem]' : ''} 
          ${animateOnHover ? 'hover:translate-y-[-2px] hover:z-10 hover:animate-out' : ''}
        `}>
        <div className="flex flex-col h-full justify-center items-center backdrop-brightness-50 p-6">
          < h1 className="text-[1.5rem] leading-[2.0rem] font-bold text-center text-white font-Lato" > {name}</h1 >

          {
            spotifyUrl ?
              <span className="absolute top-4 right-4 font-bold text-base text-white cursor-pointer"
                onClick={() => window.open(spotifyUrl, '_blank')}
              >
                <Disc3Icon></Disc3Icon >
              </span >
              :
              <></>
          }

          {
            backgroundInPrice ?
              <span className="absolute bottom-4 right-4 text-white font-bold text-lg bg-red-950 rounded-md">
                <p className="min-w-20 text-center">R$ {price}</p>
              </span>
              :
              <span className="absolute bottom-4 right-4 text-white font-bold text-lg">
                R$ {price}
              </span>
          }
        </div >
      </Card >
    </div>
  )
}
