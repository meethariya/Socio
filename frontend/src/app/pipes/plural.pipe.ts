import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'plural'
})
export class PluralPipe implements PipeTransform {

  transform(number: number, singularText: string, pluralText: string): string {
    return number <= 1 ? singularText : pluralText;
  }

}
