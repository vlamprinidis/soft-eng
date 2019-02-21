import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-contact',
  templateUrl: './contact.component.html',
  styleUrls: ['./contact.component.scss']
})
export class ContactComponent  {

  cities = [ 'type1', 'type2', 'type3' ];

  constructor() { }

  onCitySelect(event) {
    console.log(event.target.value);
  }


}
