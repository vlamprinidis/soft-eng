import { Component, OnInit, OnDestroy } from '@angular/core';
import { DataService } from '../data.service';
import { ActivatedRoute, Router } from '@angular/router';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'app-prod-update',
  templateUrl: './prod-update.component.html',
  styleUrls: ['./prod-update.component.scss']
})
export class ProdUpdateComponent implements OnInit, OnDestroy {
  id: number;
  name = '';
  description = '';
  category = '';
  withdrawn = false;
  tags = '';
  private sub: any;
  prod: Object;
  messageForm: FormGroup;
  submitted = false;
  success = false;

  constructor(private data: DataService, private route: ActivatedRoute, private router: Router, private formBuilder: FormBuilder) { }

  ngOnInit() {
    this.sub = this.route
      .queryParams
      .subscribe(params => {
        // Defaults to 0 if no query param provided.
        this.id = +params['d1'] || 0;
        this.name = params['d2'] || '';
        this.description = params['d3'] || '';
        this.category = params['d4'] || '';
        this.withdrawn = params['d6'] || false;
        this.tags = params['d5'] || '';
        console.log(this.id);
        console.log(this.name);
        console.log(this.description);
        console.log(this.category);
        console.log(this.withdrawn);
        console.log(this.tags);
      });
    this.messageForm = this.formBuilder.group({
      id: [''],
      name: ['', Validators.required],
      description: [''],
      category: ['', Validators.required],
      withdrawn: [''],
      tags: ['', Validators.required]
    });
  }

  ngOnDestroy() {
    this.sub.unsubscribe();
  }

  onSubmit() {
    this.submitted = true;
    if (this.messageForm.invalid) {
      return;
    }
    this.success = true;
    this.data.updateProduct(this.messageForm.controls.name.value, this.messageForm.controls.description.value,
      this.messageForm.controls.category.value, this.messageForm.controls.tags.value, this.id).subscribe(data => {
        this.prod = data;
        console.log(this.prod);
      }
    );
  }

}
