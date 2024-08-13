import {Evenement, EvenementDTO} from "./evenement";
import {Transaction} from "./transaction";

export interface Pari {
  id:number,
  prediction:string,
  mise:number,
  transactionId:number,
  evenementId:number
}

export interface PariDTO {
  id:number,
  evenement:EvenementDTO |undefined,
  prediction:string,
  mise:number,
  transactionId:number,
}
