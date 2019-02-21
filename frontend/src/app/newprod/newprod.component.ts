import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {DataService} from '../data.service';

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


  constructor(private formBuilder: FormBuilder, private data: DataService) { }

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
  }


}