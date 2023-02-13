# react-native-connectivity-checker

Simple module which checks location status on android and ios

## Installation

```sh
yarn add react-native-connectivity-checker
```

## Usage

```tsx
import * as React from 'react';

import { StyleSheet, Text, View } from 'react-native';
import ConnectivityChecker from 'react-native-connectivity-checker';

export default function App() {
  const [result, setResult] = React.useState<boolean>();

  React.useEffect(() => {
    ConnectivityChecker.isLocationEnabled().then((res) => {
      setResult(res);
    });

    const listener = ConnectivityChecker.addListener((res) => {
      setResult(res.status);
    });

    return () => {
      listener.remove();
    };
  }, []);

  return (
    <View style={styles.container}>
      <Text>Result: {String(result)}</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
});
```

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT
