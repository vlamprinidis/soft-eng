import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {DataService} from '../data.service';

@Component({
  selector: 'app-prod-new',
  templateUrl: './prod-new.component.html',
  styleUrls: ['./prod-new.component.scss']
})
export class ProdNewComponent implements OnInit {

  prod: Object;
  messageForm: FormGroup;
  submitted = false;
  success = false;

  category = ['Βραχυπρόθεσμο', 'Μακροπρόθεσμο (1 ημέρα και πάνω)'];

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
    console.log(this.messageForm.controls.name.value);
    this.success = true;
    if (this.messageForm.controls.category.value === 'Βραχυπρόθεσμο') {
      this.data.addProduct(this.messageForm.controls.name.value, this.messageForm.controls.description.value,
        'Βραχυπρόθεσμο', this.messageForm.controls.tags.value).subscribe(data => {
          this.prod = data;
          console.log(this.prod);
        }
      );
    } else {
      this.data.addProduct(this.messageForm.controls.name.value, this.messageForm.controls.description.value,
        'Μακροπρόθεσμο', this.messageForm.controls.tags.value).subscribe(data => {
          this.prod = data;
          console.log(this.prod);
        }
      );
    }
  }

}
