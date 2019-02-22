import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {DataService} from '../data.service';
import { Router } from '@angular/router';



@Component({
  selector: 'app-newprod',
  templateUrl: './newprod.component.html',
  styleUrls: ['./newprod.component.scss']
})
export class NewprodComponent implements OnInit {

  prod: Object;
  messageForm: FormGroup;
  submitted = false;
  success = false;


  constructor(private formBuilder: FormBuilder, private data: DataService, router: Router) { }

  ngOnInit() {
    this.messageForm = this.formBuilder.group({
      name: ['', Validators.required],
      description: [''],
      category: ['', Validators.required],
      tags: ['', Validators.required]
    });
  }

  ShowClick(id) {
    console.log('clicked');
    this.data.getProduct(id).subscribe(data => {
        this.prod = data;
        console.log(this.prod);
      }
    );
  }

  onSubmit() {
    this.submitted = true;

    if (this.messageForm.invalid) {
      return;
    }

    this.success = true;
    this.data.addProduct(this.messageForm.controls.name.value, this.messageForm.controls.description.value,
      this.messageForm.controls.category.value, this.messageForm.controls.tags.value).subscribe(data => {
        this.prod = data;
        console.log(this.prod);
      }
    );
    // router.navigateByUrl('../showprod',{ d1: this.prod.id, d2:prod.name, d3: prod.description, d4: prod.category, d5: prod.tags,
    // d6: prod.withdrawn}"

  }


}
