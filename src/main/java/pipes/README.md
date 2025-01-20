# Module Purpose
"Pipes" are the code which connect the different modules...
In a way, sort of the "main" executable / invoking code.

Except I want the actual main to be more configuration / view oriented rather than focused on making the program "work".

## Model Demultiplexer
### Element name hardcodes
| ElementType (IN) | DiagramType   | Primitive (OUT) |
|------------------|---------------|-----------------|
| message          | collaboration | stimuli         |
| lifeline         | collaboration | agent           |
