import { Component } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { InputTextModule } from 'primeng/inputtext';
import { TableModule } from 'primeng/table';
import { FileUploadModule } from 'primeng/fileupload';
import { InputNumberModule } from 'primeng/inputnumber';
import { DropdownModule } from 'primeng/dropdown';
import { FoodItem, FoodItemService } from '../../../services/food-item.service';
import { TranslateModule } from '@ngx-translate/core';

@Component({
  selector: 'app-food-item',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    HttpClientModule,
    TableModule,
    DialogModule,
    ButtonModule,
    InputTextModule,
    FileUploadModule,
    InputNumberModule,
    DropdownModule,
    TranslateModule
  ],
  templateUrl: './food-item.component.html',
  styleUrl: './food-item.component.scss',
  providers: [FoodItemService]
})
export class FoodItemComponent {
  foodItems: FoodItem[] = [];
  form: FormGroup;
  dialogVisible: boolean = false;
  isEdit: boolean = false;
  imagePreview: string | null = null;
  selectedFile: File | null = null;
  url= "http://localhost:8080/";

  constructor(
    public foodItemService: FoodItemService,
    private fb: FormBuilder
  ) {
    this.form = this.fb.group({
      id: [null],
      name: ['', Validators.required],
      price: [0, [Validators.required, Validators.min(0)]],
      imageUrl: [''],
    });
  }

  ngOnInit() {
    this.loadFoodItems();
  }

  loadFoodItems() {
    this.foodItemService.getAll().subscribe(data => this.foodItems = data);
  }

  openNew() {
    this.form.reset({
      name: '',
      price: 0,
      imageUrl: '',
    });
    this.dialogVisible = true;
    this.isEdit = false;
    this.imagePreview = null;
    this.selectedFile = null;
  }

  editItem(item: FoodItem) {
    this.form.patchValue(item);
    this.dialogVisible = true;
    this.isEdit = true;

    if (item.imageUrl) {
      const cleanedUrl = item.imageUrl.replace(/^\/+/, '');
      this.imagePreview = `${this.url}${cleanedUrl}`;
    } else {
      this.imagePreview = null;
    }

    this.selectedFile = null;
  }


  deletedItem(id: number) {
    this.foodItemService.delete(id).subscribe(() => this.loadFoodItems());
  }

  saveItem() {
    const formValue = this.form.value;
    const formData = new FormData();

    formData.append('name', formValue.name);
    formData.append('price', formValue.price.toString());

    if (this.selectedFile) {
      formData.append('image', this.selectedFile);
    }

    const request = this.isEdit
      ? this.foodItemService.updateMultipart(formValue.id, formData)
      : this.foodItemService.addMultipart(formData);

    request.subscribe(() => {
      this.loadFoodItems();
      this.dialogVisible = false;
      this.selectedFile = null;
      this.imagePreview = null;
    });
  }

  onFileSelect(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (!input || !input.files || input.files.length === 0) {
      console.warn("No file selected");
      return;
    }

    this.selectedFile = input.files[0];
    const reader = new FileReader();
    reader.onload = () => {
      this.imagePreview = reader.result as string;
    };
    reader.readAsDataURL(this.selectedFile);
  }
}
