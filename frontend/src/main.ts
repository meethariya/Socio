/// <reference types="@angular/localize" />
(window as any).global = window;
import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';
import { AppComponent } from './app/app.component';
import { environment } from './environments/environment';

import { WebTracerProvider } from '@opentelemetry/sdk-trace-web';
import { BatchSpanProcessor } from '@opentelemetry/sdk-trace-base';
import { ZipkinExporter } from '@opentelemetry/exporter-zipkin';
import { registerInstrumentations } from '@opentelemetry/instrumentation';
import { resourceFromAttributes } from '@opentelemetry/resources';
import { B3Propagator } from '@opentelemetry/propagator-b3';
import { XMLHttpRequestInstrumentation } from '@opentelemetry/instrumentation-xml-http-request';
import { FetchInstrumentation } from '@opentelemetry/instrumentation-fetch';

const provider = new WebTracerProvider({
  resource: resourceFromAttributes({
    'service.name': 'angular-ui',
    'environment': environment.production ? 'production' : 'development',
  }),
  spanProcessors: [
    new BatchSpanProcessor(
      new ZipkinExporter({
        url: 'http://localhost:9411/api/v2/spans',
        serviceName: 'angular-ui',
      })
    ),
  ],
});
provider.register({
  propagator: new B3Propagator(),
});
registerInstrumentations({
  instrumentations: [
    // new DocumentLoadInstrumentation(),   // page‐load spans
    new XMLHttpRequestInstrumentation({
      propagateTraceHeaderCorsUrls: [new RegExp(`^${environment.baseUrl}`)],
    }),
    new FetchInstrumentation({
      propagateTraceHeaderCorsUrls: [new RegExp(`^${environment.baseUrl}`)],
    }),
  ],
});

bootstrapApplication(AppComponent, appConfig)
  .catch((err) => console.error(err));
